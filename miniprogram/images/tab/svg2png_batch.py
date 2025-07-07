import os
import cairosvg

# 获取当前目录下所有svg文件
svg_files = [f for f in os.listdir('.') if f.endswith('.svg')]

for svg in svg_files:
    png = svg.replace('.svg', '.png')
    print(f"转换: {svg} -> {png}")
    cairosvg.svg2png(url=svg, write_to=png)

# 删除没有对应svg的png
for png in [f for f in os.listdir('.') if f.endswith('.png')]:
    svg = png.replace('.png', '.svg')
    if not os.path.exists(svg):
        print(f"删除无效PNG: {png}")
        os.remove(png)

print("SVG转PNG并清理无效PNG完成！") 